using System;
using System.ComponentModel;
using System.IO;
using System.Net;
using System.Text;
using System.Windows;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using bitkyShop.bean;


namespace bitkyShop.view
{
    /// <summary>
    /// ConfirmUploadInfo.xaml 的交互逻辑
    /// </summary>
    public partial class ConfirmUploadCommodityInfo : Window
    {
        private static ConfirmUploadCommodityInfo _confirmUploadCommodityInfo;
        private Commodity _commodity;
        private string _photoLocalUrl;
        private WebClient _webClient;
        private static ICloudServiceView _window;

        private ConfirmUploadCommodityInfo()
        {
            InitializeComponent();
            _confirmUploadCommodityInfo = this;
        }

        public static ConfirmUploadCommodityInfo Builder(ICloudServiceView window)
        {
            _window = window;
            return _confirmUploadCommodityInfo != null ? _confirmUploadCommodityInfo : new ConfirmUploadCommodityInfo();
        }

        public ConfirmUploadCommodityInfo SetCommodityInfo(Commodity commodity)
        {
            _commodity = commodity;
            labelName.Content = commodity.Name;
            labelCategory.Content = commodity.Category;
            labelCategorySub.Content = commodity.CategorySub;
            labelCount.Content = commodity.Count;
            labelPrice.Content = commodity.Price;
            textBlockDetails.Text = commodity.Details;
            var builder = new StringBuilder();
            if (commodity.Promotion != null && commodity.Promotion.Equals("true"))
            {
                builder.AppendLine("已促销");
            }
            if (commodity.AD != null && commodity.AD.Equals("true"))
            {
                builder.AppendLine("已设为广告");
            }
            if (builder.Length <= 2)
            {
                builder.Append("无");
            }
            labelPromotionAndAD.Content = builder;

            _photoLocalUrl = @"./photoCache/" + commodity.CoverPhotoName;
            _webClient = new WebClient();
            _webClient.DownloadFileAsync(new Uri(commodity.CoverPhotoUrl), _photoLocalUrl);
            _webClient.DownloadFileCompleted += DownloadFileCompleted;
            imageShow.Stretch = Stretch.Uniform;

            var info = new FileInfo(@"./resource/defaultCoverPhoto");
            if (info.Exists)
                imageShow.Source = new BitmapImage((new Uri(info.FullName, UriKind.Absolute)));
            return this;
        }

        /// <summary>
        /// 图片下载完成回调
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="asyncCompletedEventArgs"></param>
        private void DownloadFileCompleted(object sender, AsyncCompletedEventArgs asyncCompletedEventArgs)
        {
            var info = new FileInfo(_photoLocalUrl);
            if (info.Exists)
            {
                imageShow.Source = new BitmapImage(new Uri(info.FullName, UriKind.Absolute));
            }
        }

        private void Window_MouseLeftButtonDown(object sender, MouseButtonEventArgs e)
        {
            DragMove();
        }

        private void Window_Closed(object sender, EventArgs e)
        {
            _confirmUploadCommodityInfo = null;
        }

        private void btnClose_Click(object sender, RoutedEventArgs e)
        {
            Close();
        }

        private void btnConfirmUpload_Click(object sender, RoutedEventArgs e)
        {
            _window.ConfirmUpload();
        }

        public void UploadSuccessful()
        {
            Close();
            MessageBox.Show("商品上传成功", "提示");
        }

        public void UploadFailed(string msg)
        {
            MessageBox.Show("商品上传失败\n错误信息: " + msg, "提示");
        }
    }
}