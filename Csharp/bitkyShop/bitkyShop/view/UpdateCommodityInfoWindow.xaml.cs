using System;
using System.Collections.Generic;
using System.Linq;
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
using bitkyShop.utils;

namespace bitkyShop.view
{
    /// <summary>
    /// UpdateCommodityInfoWindow.xaml 的交互逻辑
    /// </summary>
    public partial class UpdateCommodityInfoWindow : Window
    {
        private Commodity _commodity;
        private ICloudServiceView _view;

        public UpdateCommodityInfoWindow(ICloudServiceView view, Commodity commodity)
        {
            InitializeComponent();
            _commodity = commodity;
            _view = view;

            textBoxName.Text = commodity.Name;
            textBoxCategory.Text = commodity.Category;
            textBoxSubCategory.Text = commodity.CategorySub;
            textBoxCount.Text = commodity.Count.ToString();
            textBoxPrice.Text = commodity.Price.ToString();
            textBoxDetails.Text = commodity.Details;
        }

        private void Window_MouseLeftButtonDown(object sender, MouseButtonEventArgs e)
        {
            DragMove();
        }

        private void btnClose_Click(object sender, RoutedEventArgs e)
        {
            Close();
        }

        private void btnConfirmUpdate_Click(object sender, RoutedEventArgs e)
        {
            if (textBoxName.Text.Trim().Equals(string.Empty) ||
                textBoxCategory.Text.Trim().Equals(string.Empty) ||
                !KyMatch.IsInt(textBoxCount.Text.Trim()) ||
                !KyMatch.IsFloat(textBoxPrice.Text.Trim()))
            {
                MessageBox.Show("输入文本有误，请检查！", "警告");
                return;
            }
            if (textBoxSubCategory.Text.Trim().Equals(string.Empty))
            {
                var category = textBoxCategory.Text.Trim();
                if (
                    !(category.Equals("水果") || category.Equals("烧烤") || category.Equals("旅游") ||
                      category.Equals("广告与促销")))
                {
                    MessageBox.Show("输入文本有误，请检查！", "警告");
                    return;
                }
            }
            Commodity commodity = new Commodity
            {
                Name = textBoxName.Text.Trim(),
                Category = textBoxCategory.Text.Trim(),
                CategorySub = textBoxSubCategory.Text.Trim(),
                Count = int.Parse(textBoxCount.Text.Trim()),
                Price = double.Parse(textBoxPrice.Text.Trim()),
                Details = textBoxDetails.Text.Trim()
            };

            commodity.objectId = _commodity.objectId;
            Close();
            _view.updateCommodity(commodity);
        }
    }
}