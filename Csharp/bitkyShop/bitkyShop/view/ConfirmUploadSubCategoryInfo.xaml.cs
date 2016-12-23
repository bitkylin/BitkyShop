using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.IO;
using System.Net;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Shapes;
using bitkyShop.bean;

namespace bitkyShop.view
{
    /// <summary>
    /// ConfirmUploadSubCategoryInfo.xaml 的交互逻辑
    /// </summary>
    public partial class ConfirmUploadSubCategoryInfo : Window
    {
        private static ConfirmUploadSubCategoryInfo _confirmUploadSubCategoryInfo;
        private string _photoLocalUrl;
        private WebClient _webClient;
        private static ICloudServiceView _window;
        private SubCategory _subCategory;


        public ConfirmUploadSubCategoryInfo()
        {
            InitializeComponent();
            _confirmUploadSubCategoryInfo = this;
        }

        public static ConfirmUploadSubCategoryInfo Builder(ICloudServiceView window)
        {
            _window = window;
            return _confirmUploadSubCategoryInfo != null
                ? _confirmUploadSubCategoryInfo
                : new ConfirmUploadSubCategoryInfo();
        }

        public ConfirmUploadSubCategoryInfo SetSubCategoryInfo(SubCategory subCategory)
        {
            _subCategory = subCategory;
            labelName.Content = subCategory.name;
            labelCategory.Content = subCategory.mainCategory;
            var info = new FileInfo(@"./resource/defaultCoverPhoto");
            if (info.Exists)
                imageShow.Source = new BitmapImage((new Uri(info.FullName, UriKind.Absolute)));

            var builder = new StringBuilder();
            _photoLocalUrl = @"./photoCache/" + subCategory.photoName;
            _webClient = new WebClient();
            _webClient.DownloadFileAsync(new Uri(subCategory.photoUrl), _photoLocalUrl);
            _webClient.DownloadFileCompleted += DownloadFileCompleted;
            imageShow.Stretch = Stretch.Uniform;
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

        private void btnClose_Click(object sender, RoutedEventArgs e)
        {
            Close();
        }
        private void Window_Closed(object sender, EventArgs e)
        {
            _confirmUploadSubCategoryInfo = null;
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