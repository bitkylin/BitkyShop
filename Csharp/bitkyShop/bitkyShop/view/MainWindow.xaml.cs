using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Windows;
using bitkyShop.bean;
using bitkyShop.bean.beanShow;
using bitkyShop.init;
using bitkyShop.utils;
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
        private readonly List<OrderShow> _orderShows = new List<OrderShow>();
        private readonly List<CommodityPcShow> _orderCommoditylist = new List<CommodityPcShow>();
        private readonly List<Commodity> _commodities = new List<Commodity>();
        private readonly List<SubCategory> _subCategories = new List<SubCategory>();

        public MainWindow()
        {
            InitializeComponent();
            _presenter = new CommPresenter(this);

            dataGridOrderInfoShow.ItemsSource = _orderShows;
            dataGridOrderCommodityInfoShow.ItemsSource = _orderCommoditylist;
            dataGridCommodity.ItemsSource = _commodities;
            dataGridSubCategory.ItemsSource = _subCategories;
        }

        /// <summary>
        ///     七牛上传完毕事件处理
        /// </summary>
        /// <param name="key">已上传的文件名</param>
        /// <param name="respInfo">返回信息</param>
        /// <param name="respJson">返回Json</param>
        public void OnQiniuUploadCompleted(string key, ResponseInfo respInfo, string respJson)
        {
            Dispatcher.Invoke(new Action(() =>
            {
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
                    Debug.WriteLine("图片上传出错");
                }
            }));
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
                default:
                    throw new Exception("程序错误，请检查");
            }
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
                !KyMatch.IsInt(TextBoxCount.Text.Trim()) ||
                !KyMatch.IsFloat(TextBoxPrice.Text.Trim()))
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

        private void btnQueryOrder_Click(object sender, RoutedEventArgs e)
        {
            if (btnQueryOrder.Content.Equals("开始查询"))
            {
                btnQueryOrderCategory.IsEnabled = false;
                btnQueryOrder.Content = "停止查询";
                _presenter.queryOrderInLoop();
            }
            else
            {
                btnQueryOrderCategory.IsEnabled = true;
                btnQueryOrder.Content = "开始查询";
                _presenter.stopQueryOrderInLoop();
            }
        }

        private void btnQueryOrderCategory_Click(object sender, RoutedEventArgs e)
        {
            if (comboBoxOrderCategory.Text.Equals("全部"))
            {
                _presenter.QueryOrder(OrderStatus.NONE);
                return;
            }
            if (comboBoxOrderCategory.Text.Equals("已下单"))
            {
                _presenter.QueryOrder(OrderStatus.已下单);
                return;
            }
            if (comboBoxOrderCategory.Text.Equals("已送达"))
            {
                _presenter.QueryOrder(OrderStatus.已送达);
                return;
            }
            if (comboBoxOrderCategory.Text.Equals("已确认收货"))
            {
                _presenter.QueryOrder(OrderStatus.已确认收货);
                return;
            }
        }

        public void orderShow(List<Order> orders)
        {
            Dispatcher.Invoke(new Action(() =>
            {
                if (orders != null)
                {
                    _orderShows.Clear();
                    labelOrderStatusShow.Content = DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss") + "     " + "共查询到" +
                                                   orders.Count + "条数据";
                    orders.ForEach(order => { _orderShows.Add(new OrderShow(order)); });
                    dataGridOrderInfoShow.ItemsSource = null;
                    dataGridOrderInfoShow.ItemsSource = _orderShows;
                }
                else
                {
                    MessageBox.Show("未查询到数据");
                    dataGridOrderInfoShow.ItemsSource = null;
                }
            }));
        }

        public void commodityShow(List<Commodity> commodities)
        {
            Dispatcher.Invoke(new Action(() =>
            {
                if (commodities != null)
                {
                    _commodities.Clear();
                    labelCommodityStatusShow.Content = DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss") + "     " + "共查询到" +
                                                       commodities.Count + "条数据";
                    commodities.ForEach(commodity => { _commodities.Add(commodity); });
                    dataGridCommodity.ItemsSource = null;
                    dataGridCommodity.ItemsSource = _commodities;
                }
                else
                {
                    MessageBox.Show("未查询到数据");
                    dataGridOrderInfoShow.ItemsSource = null;
                }
            }));
        }

        public void subCategoryShow(List<SubCategory> subCategories)
        {
            Dispatcher.Invoke(new Action(() =>
            {
                if (subCategories != null)
                {
                    _subCategories.Clear();
                    labelSubCategoryStatusShow.Content = DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss") + "     " +
                                                         "共查询到" +
                                                         subCategories.Count + "条数据";
                    subCategories.ForEach(subCategory => { _subCategories.Add(subCategory); });
                    dataGridSubCategory.ItemsSource = null;
                    dataGridSubCategory.ItemsSource = _subCategories;
                }
                else
                {
                    MessageBox.Show("未查询到数据");
                    dataGridOrderInfoShow.ItemsSource = null;
                }
            }));
        }

        public void updateCommodity(Commodity commodity)
        {
            _presenter.UpdateCommodity(commodity);
        }

        public void addOrderCommodity(CommodityPcShow commodityPcShow)
        {
            Dispatcher.Invoke(new Action(() =>
            {
                _orderCommoditylist.Add(commodityPcShow);
                Debug.WriteLine("orderCommoditylist.Count:" + _orderCommoditylist.Count);

                dataGridOrderCommodityInfoShow.ItemsSource = null;
                dataGridOrderCommodityInfoShow.ItemsSource = _orderCommoditylist;
            }));
        }


        private enum UploadPhotoType
        {
            Commodity,
            Subcategory
        }

        private void dataGridOrderInfoShow_MouseDoubleClick(object sender, System.Windows.Input.MouseButtonEventArgs e)
        {
            _orderCommoditylist.Clear();
            var item = dataGridOrderInfoShow.SelectedItem as OrderShow;
            if (item == null)
            {
                MessageBox.Show("请选择一条订单");
                return;
            }

            _presenter.QueryOrderCommodity(item.commodityList);
        }

        private void btnQueryCommodity_Click(object sender, RoutedEventArgs e)
        {
            if (ComboBoxCategory_changeCommodity.Text.Equals(""))
            {
                MessageBox.Show("请选择商品的总类别");
                return;
            }
            string category = ComboBoxCategory_changeCommodity.Text;
            string subcategory = TextBoxCategorySub_changeCommodity.Text;
            if (subcategory.Equals(""))
            {
                subcategory = null;
            }

            _presenter.queryCommodity(category, subcategory);
        }

        private void btnChangeCommodityToBmob_Click(object sender, RoutedEventArgs e)
        {
            Commodity commodity = dataGridCommodity.SelectedItem as Commodity;
            if (commodity == null)
            {
                MessageBox.Show("请选择一条商品信息");
                return;
            }
            new UpdateCommodityInfoWindow(this, commodity).Show();
        }

        private void btnDeleteSubCategory_Click(object sender, RoutedEventArgs e)
        {
            SubCategory subCategory = dataGridSubCategory.SelectedItem as SubCategory;
            if (subCategory == null)
            {
                MessageBox.Show("请选择一条二级类别信息");
                return;
            }
            _presenter.DeleteItemFromBomb("SubCategory", subCategory.objectId);
        }

        private void btnQuerySubCategory_Click(object sender, RoutedEventArgs e)
        {
            string category = ComboBoxCategory_querySubCategory.Text;
            _presenter.querySubCategory(category);
        }

        private void btnDeleteCommodity_Click(object sender, RoutedEventArgs e)
        {
            if (
                MessageBox.Show(
                    "注意：没有特殊原因不要删除商品，删除商品会可能导致手机App的信息显示出现不可预测的问题。\r\n如果不想在手机App中显示，请将此商品的数量修改为0\r\n您确定要删除本商品吗？", "警告",
                    MessageBoxButton.OKCancel) == MessageBoxResult.Cancel)
            {
                return;
            }
            {
            }
            Commodity commodity = dataGridCommodity.SelectedItem as Commodity;
            if (commodity == null)
            {
                MessageBox.Show("请选择一条商品信息");
                return;
            }
            _presenter.DeleteItemFromBomb("Commodity", commodity.objectId);
        }

        private void btnQueryOrderMarkArrived_Click(object sender, RoutedEventArgs e)
        {
            OrderShow orderShow = dataGridOrderInfoShow.SelectedItem as OrderShow;
            if (orderShow == null)
            {
                MessageBox.Show("请选择一条订单");
                return;
            }
            Order order = new Order();
            order.objectId = orderShow.objectId;
            order.status = OrderStatus.已送达;
            _presenter.UpdateOrderToArrived(order);
        }

        private void button_Click(object sender, RoutedEventArgs e)
        {
            _presenter.queryCommodityNative(0);
        }

        private void button_Copy_Click(object sender, RoutedEventArgs e)
        {
            _presenter.querySubCategoryNative(0);
        }

        private void button_removeJpeg_Click(object sender, RoutedEventArgs e)
        {
            var dir = Environment.CurrentDirectory + "/photoCache/";
            String[] files = Directory.GetFiles(dir);
            foreach (var file in files)
            {
                File.Move(file, file.Substring(0, file.Length - 4));
            }
        }

        private void button_addJpeg_Click(object sender, RoutedEventArgs e)
        {
            var dir = Environment.CurrentDirectory + "/photoCache/";
            String[] files = Directory.GetFiles(dir);
            foreach (var file in files)
            {
                File.Move(file, file + ".jpg");
            }
        }
    }
}