using System.Collections.Generic;
using System.Diagnostics;
using System.Timers;
using System.Windows;
using System.Windows.Documents;
using bitkyShop.bean;
using bitkyShop.bean.beanShow;
using bitkyShop.view;
using cn.bmob.api;
using cn.bmob.io;
using Qiniu.Http;

namespace bitkyShop.init
{
    public class CommPresenter
    {
        private readonly ICloudServiceView _view;
        private readonly CloudServiceHelper _cloudServiceHelper;
        private readonly BmobWindows _bmobWindows;
        private Timer _timer;
        private List<Order> holdingOrders;

        public CommPresenter(ICloudServiceView view)
        {
            _view = view;
            _cloudServiceHelper = new CloudServiceHelper(this);
            _bmobWindows = _cloudServiceHelper.BmobBuilder();
            initTimer();
        }

        public void UploadFile(string filePath)
        {
            _cloudServiceHelper.UploadFile(filePath);
        }

        public void OnQiniuUploadCompleted(string key, ResponseInfo respInfo, string respJson)
        {
            _view.OnQiniuUploadCompleted(key, respInfo, respJson);
        }

        public void UploadCommodityOrCategoryData(BmobTable data)
        {
            var fature = _bmobWindows.CreateTaskAsync(data);
            if (fature.Exception == null)
            {
                Debug.WriteLine("上传成功，createdAt:" + fature.Result.createdAt + ".objectId:" + fature.Result.objectId);
                _view.OnBmobUploadCompleted(0, fature.Result.objectId);
            }
            else
            {
                Debug.WriteLine("上传失败:" + fature.Exception.Message);
                _view.OnBmobUploadCompleted(1, fature.Exception.Message);
            }
        }

        public void queryOrderInLoop()
        {
            _timer.Enabled = true;
            holdingOrders = new List<Order>();
            QueryOrder(OrderStatus.已下单);
        }

        public void stopQueryOrderInLoop()
        {
            _timer.Enabled = false;
        }

        private void Timer_queryInLoop(object sender, ElapsedEventArgs e)
        {
            QueryOrder(OrderStatus.已下单);
        }

        private void initTimer()
        {
            _timer = new Timer(30000);
            _timer.AutoReset = true;
            _timer.Enabled = false;
            _timer.Elapsed += Timer_queryInLoop;
        }

        public void QueryOrder(int orderStatus)
        {
            var bmobQuery = new BmobQuery();
            if (orderStatus != OrderStatus.NONE)
            {
                bmobQuery.WhereEqualTo("status", orderStatus);
            }
            _bmobWindows.Find<Order>("Order", bmobQuery, (resp, ex) =>
            {
                if (ex != null)
                {
                    Debug.WriteLine("查询出错:" + ex.Message);
                    return;
                }
                var orders = resp.results;
                _view.orderShow(orders);
                //对比前后订单列表的不同
                if (orderStatus == OrderStatus.已下单)
                {
                    Debug.WriteLine("检索已下单");
                    orders.ForEach(order =>
                    {
                        var objectId = order.objectId;
                        var isNew = true;

                        holdingOrders.ForEach(holdOrder =>
                        {
                            if (objectId.Equals(holdOrder.objectId))
                            {
                                isNew = false;
                            }
                        });
                        if (isNew)
                        {
                            Debug.WriteLine("有新订单来了，请注意查收");
                            var sndPlayer = new System.Media.SoundPlayer(@"./resource/kyOrderSound");
                            sndPlayer.Play();
                        }
                    });
                    holdingOrders = orders;
                }
            });
        }

        public void QueryOrderCommodity(List<CommodityOrder> commodityOrders)
        {
            commodityOrders.ForEach(commodityOrder =>
            {
                BmobQuery bmobQuery = new BmobQuery();
                bmobQuery.WhereEqualTo("objectId", commodityOrder.objectId);
                _bmobWindows.Find<Commodity>("Commodity", bmobQuery, (result, ex) =>
                    {
                        if (ex != null)
                        {
                            Debug.WriteLine("错误:" + ex.Message);
                        }
                        else
                        {
                            _view.addOrderCommodity(new CommodityPcShow(result.results[0], commodityOrder));
                        }
                    }
                );
            });
        }

        public void queryCommodity(string category, string subcategory)
        {
            var bmobQuery = new BmobQuery();

            bmobQuery.WhereEqualTo("Category", category);
            if (subcategory != null)
            {
                bmobQuery.WhereEqualTo("CategorySub", subcategory);
            }

            _bmobWindows.Find<Commodity>("Commodity", bmobQuery, (resp, ex) =>
            {
                if (ex != null)
                {
                    Debug.WriteLine("查询出错:" + ex.Message);
                    return;
                }
                var commodities = resp.results;
                _view.commodityShow(commodities);
            });
        }

        public void UpdateCommodity(Commodity commodity)
        {
            _bmobWindows.Update(commodity, (responseInfo, exception) =>
            {
                if (exception != null)
                {
                    Debug.WriteLine("修改失败: " + exception.Message);
                    MessageBox.Show("修改失败: " + exception.Message);
                    return;
                }
                MessageBox.Show("修改成功");
            });
        }

        public void UpdateOrderToArrived(Order order)
        {
            _bmobWindows.Update(order, (responseInfo, exception) =>
            {
                if (exception != null)
                {
                    Debug.WriteLine("修改失败: " + exception.Message);
                    MessageBox.Show("修改失败: " + exception.Message);
                    return;
                }
                MessageBox.Show("订单状态已修改成功");
            });
        }

        public void querySubCategory(string category)
        {
            var bmobQuery = new BmobQuery();
            if (!category.Equals(""))
            {
                bmobQuery.WhereEqualTo("mainCategory", category);
            }
            _bmobWindows.Find<SubCategory>("SubCategory", bmobQuery, (resp, ex) =>
            {
                if (ex != null)
                {
                    Debug.WriteLine("查询出错:" + ex.Message);
                    MessageBox.Show("查询出错:" + ex.Message);
                    return;
                }
                var subCategories = resp.results;
                _view.subCategoryShow(subCategories);
            });
        }

        public void DeleteItemFromBomb(string tableName, string objectId)
        {
            _bmobWindows.Delete(tableName, objectId, (resp, exception) =>
            {
                if (exception != null)
                {
                    MessageBox.Show(exception.Message);
                    return;
                }
                MessageBox.Show("删除成功");
            });
        }
    }
}