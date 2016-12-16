using System;
using System.Diagnostics;
using System.Text.RegularExpressions;
using System.Windows;
using bitkyShop.bean;
using bitkyShop.init;
using Microsoft.Win32;
using Qiniu.Http;

namespace bitkyShop.view
{
    /// <summary>
    ///     MainWindow.xaml 的交互逻辑
    /// </summary>
    public partial class MainWindow : ICloudServiceView
    {
        private readonly CommPresenter _presenter;
        private Commodity _commodity;
        private ConfirmUploadCommodityInfo _confirmUploadCommodityInfo;
        private ConfirmUploadSubCategoryInfo _confirmUploadSubCategoryInfo;
        private string _filePath = string.Empty;
        private string _filePathSubcategory = string.Empty;
        private UploadPhotoType _currentUploadPhotoType = UploadPhotoType.Commodity;
        private SubCategory _subCategory;

        public MainWindow()
        {
            InitializeComponent();
            _presenter = new CommPresenter(this);
        }

        /// <summary>
        ///     七牛上传完毕事件处理
        /// </summary>
        /// <param name="key">已上传的文件名</param>
        /// <param name="respInfo">返回信息</param>
        /// <param name="respJson">返回Json</param>
        public void OnQiniuUploadCompleted(string key, ResponseInfo respInfo, string respJson)
        {
            Dispatcher.Invoke(() =>
            {
                CommunicateMessageShow("图片上传完毕");
                CommunicateMessageShow(respJson);
                if (respInfo.StatusCode == 200)
                {
                    switch (_currentUploadPhotoType)
                    {
                        case UploadPhotoType.Commodity:
                            _commodity.CoverPhotoName = key;
                            _commodity.CoverPhotoUrl = PresetInfo.UrlBase + key;
                            break;
                        case UploadPhotoType.Subcategory:
                            _subCategory.photoName = key;
                            _subCategory.photoUrl = PresetInfo.UrlBase + key;
                            break;
                    }

                    UploadBmobWindowShow();
                }
                else
                {
                    CommunicateMessageShow("图片上传出错");
                }
            });
        }

        /// <summary>
        ///     确认单个商品信息上传Bmob
        /// </summary>
        public void ConfirmUpload()
        {
            switch (_currentUploadPhotoType)
            {
                case UploadPhotoType.Commodity:
                    _presenter.UploadCommodityOrCategoryData(_commodity);
                    break;
                case UploadPhotoType.Subcategory:
                    _presenter.UploadCommodityOrCategoryData(_subCategory);
                    break;
            }
        }

        /// <summary>
        ///     Bmob上传完毕回调
        /// </summary>
        /// <param name="code">错误代码，0:成功，1:失败</param>
        /// <param name="msg">返回信息</param>
        public void OnBmobUploadCompleted(int code, string msg)
        {
            Debug.WriteLine("商品上传成功:" + code);
            switch (_currentUploadPhotoType)
            {
                case UploadPhotoType.Commodity:
                    switch (code)
                    {
                        case 0:
                            _confirmUploadCommodityInfo.UploadSuccessful();
                            break;
                        case 1:
                            _confirmUploadCommodityInfo.UploadFailed(msg);
                            break;
                    }
                    break;
                case UploadPhotoType.Subcategory:
                    switch (code)
                    {
                        case 0:
                            _confirmUploadSubCategoryInfo.UploadSuccessful();
                            break;
                        case 1:
                            _confirmUploadSubCategoryInfo.UploadFailed(msg);
                            break;
                    }
                    break;
            }

           
        }

        public void UploadBmobWindowShow()
        {
            switch (_currentUploadPhotoType)
            {
                case UploadPhotoType.Commodity:
                    if (_commodity != null)
                    {
                        _confirmUploadCommodityInfo = ConfirmUploadCommodityInfo.Builder(this)
                            .SetCommodityInfo(_commodity);
                        _confirmUploadCommodityInfo.Show();
                        return;
                    }
                    break;
                case UploadPhotoType.Subcategory:
                           _confirmUploadSubCategoryInfo = ConfirmUploadSubCategoryInfo.Builder(this)
                            .SetSubCategoryInfo(_subCategory);
                    _confirmUploadSubCategoryInfo.Show();

                    break;
            }

            throw new Exception("程序错误，请检查");
        }

        /// <summary>
        ///     通信信息的显示
        /// </summary>
        /// <param name="message">输入所需显示的信息</param>
        public void CommunicateMessageShow(string message) //通信信息
        {
            Dispatcher.Invoke(() =>
            {
                ListBoxCommunicationText.Items.Add(message);
                ListBoxCommunicationText.SelectedIndex = ListBoxCommunicationText.Items.Count - 1;
                ListBoxCommunicationText.ScrollIntoView(
                    ListBoxCommunicationText.Items[ListBoxCommunicationText.Items.Count - 1]);
            });
        }

        /// <summary>
        ///     控制信息的显示
        /// </summary>
        /// <param name="message">输入所需显示的信息</param>
        public void ControlMessageShow(string message)
        {
            Dispatcher.Invoke(() =>
            {
                ListBoxControlText.Items.Add(message);
                ListBoxControlText.SelectedIndex = ListBoxControlText.Items.Count - 1;
                ListBoxControlText.ScrollIntoView(ListBoxControlText.Items.CurrentItem);
            });
        }

        public void btnSelectedFile_Click(object sender, RoutedEventArgs e)
        {
            var fileDialog = new OpenFileDialog();
            fileDialog.Title = "打开一张图片";
            fileDialog.InitialDirectory = Environment.CurrentDirectory;
            fileDialog.Multiselect = false;
            fileDialog.DefaultExt = ".jpg";
            fileDialog.Filter = "图片文件 (*.jpg,*.png,*.jpeg)|*.jpg;*.png;*.jpeg";
            if (fileDialog.ShowDialog() != true)
            {
                LabelFileName.Content = "未选择图片";
                return;
            }
            _filePath = fileDialog.FileName;
            var fileName = fileDialog.SafeFileName;
            LabelFileName.Content = fileName;
        }

        private void BtnSelectedFileSubcategory_Click(object sender, RoutedEventArgs e)
        {
            var fileDialog = new OpenFileDialog();
            fileDialog.Title = "打开一张图片";
            fileDialog.InitialDirectory = Environment.CurrentDirectory;
            fileDialog.Multiselect = false;
            fileDialog.DefaultExt = ".jpg";
            fileDialog.Filter = "图片文件 (*.jpg,*.png,*.jpeg)|*.jpg;*.png;*.jpeg";
            if (fileDialog.ShowDialog() != true)
            {
                LabelFileName.Content = "未选择图片";
                return;
            }
            _filePathSubcategory = fileDialog.FileName;
            var fileName = fileDialog.SafeFileName;
            LabelFileNameSubcategory.Content = fileName;
        }

        public void btnUploadFile_Click(object sender, RoutedEventArgs e)
        {
            if (LabelFileName.Content.ToString().Trim().Equals(string.Empty) ||
                TextBoxName.Text.Trim().Equals(string.Empty) ||
                ComboBoxCategory.Text.Trim().Equals(string.Empty) ||
                !IsInt(TextBoxCount.Text.Trim()) ||
                !IsFloat(TextBoxPrice.Text.Trim()))
            {
                MessageBox.Show("输入文本有误，请检查！", "警告");
                return;
            }
            if (TextBoxCategorySub.Text.Trim().Equals(string.Empty))
            {
                var category = ComboBoxCategory.Text.Trim();
                if (
                    !(category.Equals("水果") || category.Equals("烧烤") || category.Equals("旅游") ||
                      category.Equals("广告与促销")))
                {
                    MessageBox.Show("输入文本有误，请检查！", "警告");
                    return;
                }
            }
            _commodity = new Commodity
            {
                Name = TextBoxName.Text.Trim(),
                Category = ComboBoxCategory.Text.Trim(),
                CategorySub = TextBoxCategorySub.Text.Trim(),
                Count = int.Parse(TextBoxCount.Text.Trim()),
                Price = double.Parse(TextBoxPrice.Text.Trim()),
                Details = TextBoxDetails.Text.Trim()
            };


            if (checkBoxPromotion.IsChecked == true)
                _commodity.Promotion = "true";
            if (checkBoxAD.IsChecked == true)
                _commodity.AD = "true";
            _currentUploadPhotoType = UploadPhotoType.Commodity;
            _presenter.UploadFile(_filePath);
        }

        private void BtnUploadFileSubCategory_Click(object sender, RoutedEventArgs e)
        {
            if (LabelFileNameSubcategory.Content.ToString().Trim().Equals(string.Empty) ||
                ComboBoxCategory2.Text.Trim().Equals(string.Empty) ||
                TextBoxNameSubcategory.Text.Trim().Equals(string.Empty))
            {
                MessageBox.Show("输入文本有误，请检查！", "警告");
                return;
            }
            _subCategory = new SubCategory
            {
                mainCategory = ComboBoxCategory2.Text.Trim(),
                name = TextBoxNameSubcategory.Text.Trim()
            };
            _currentUploadPhotoType = UploadPhotoType.Subcategory;
            _presenter.UploadFile(_filePathSubcategory);
        }


        /// <summary>
        ///     判断是自然数
        /// </summary>
        /// <param name="value">待匹配的文本</param>
        /// <returns>匹配结果</returns>
        private static bool IsInt(string value) //判断是自然数
        {
            value = value.Trim();
            return Regex.IsMatch(value, @"^[1-9]\d*|0$");
        }

        /// <summary>
        ///     判断是正浮点数
        /// </summary>
        /// <param name="value">待匹配的文本</param>
        /// <returns>匹配结果</returns>
        private static bool IsFloat(string value) //判断是正浮点数或自然数
        {
            value = value.Trim();
            return Regex.IsMatch(value, @"^[1-9]d*.d*|0.d*[1-9]d*|[1-9]\d*|0$");
        }

        private void btnChangeCommodityQueryShow_Click(object sender, RoutedEventArgs e)
        {
        }
    }

    internal enum UploadPhotoType
    {
        Commodity,
        Subcategory
    }
}