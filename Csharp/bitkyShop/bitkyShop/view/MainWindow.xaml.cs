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
    /// MainWindow.xaml 的交互逻辑
    /// </summary>
    public partial class MainWindow : ICloudServiceView
    {
        private string _filePath = string.Empty;
        private Commodity _commodity;
        private readonly CommPresenter _presenter;
        private ConfirmUploadInfo _confirmUploadInfo;

        public MainWindow()
        {
            InitializeComponent();
            _presenter = new CommPresenter(this);
        }

        /// <summary>
        ///  七牛上传完毕事件处理
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
                    _commodity.CoverPhotoName = key;
                    _commodity.CoverPhotoUrl = PresetInfo.UrlBase + key;
                    UploadBmobWindowShow();
                }
                else
                {
                    CommunicateMessageShow("图片上传出错");
                }
            });
        }

        /// <summary>
        /// 确认单个商品信息上传Bmob
        /// </summary>
        public void ConfirmUpload()
        {
            _presenter.UploadCommodityData(_commodity);
        }

        /// <summary>
        /// Bmob上传完毕回调
        /// </summary>
        /// <param name="code">错误代码，0:成功，1:失败</param>
        /// <param name="msg">返回信息</param>
        public void OnBmobUploadCompleted(int code, string msg)
        {
            if (_confirmUploadInfo == null) return;
            Debug.WriteLine("商品上传成功:"+code);
            switch (code)
            {
                case 0:
                    _confirmUploadInfo.UploadSuccessful();
                    break;
                case 1:
                    _confirmUploadInfo.UploadFailed(msg);
                    break;
            }
        }

        public void UploadBmobWindowShow()
        {
            if (_commodity != null)
            {
                _confirmUploadInfo = ConfirmUploadInfo.Builder(this)
                    .SetCommodityInfo(_commodity);
                _confirmUploadInfo.Show();
            }
            else
            {
                throw new Exception("程序错误，请检查");
            }
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

        public void btnUploadFile_Click(object sender, RoutedEventArgs e)
        {
            if (LabelFileName.Content.ToString().Equals(string.Empty) ||
                TextBoxName.Text.Equals(string.Empty) ||
                ComboBoxCategory.Text.Equals(string.Empty) ||
                TextBoxName.Text.Equals(string.Empty) ||
                !IsInt(TextBoxCount.Text) ||
                !IsFloat(TextBoxPrice.Text))
            {
                MessageBox.Show("输入文本有误，请检查！", "警告");
                return;
            }

            _commodity = new Commodity
            {
                Name = TextBoxName.Text.Trim(),
                Category = ComboBoxCategory.Text.Trim(),
                Count = int.Parse(TextBoxCount.Text.Trim()),
                Price = double.Parse(TextBoxPrice.Text.Trim()),
                Details = TextBoxDetails.Text.Trim(),
            };
            _presenter.UploadFile(_filePath);
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
    }
}