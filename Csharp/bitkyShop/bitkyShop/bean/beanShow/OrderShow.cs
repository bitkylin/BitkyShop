using System.Collections.Generic;
using System.Windows.Documents;
using cn.bmob.io;

namespace bitkyShop.bean.beanShow
{
    public class OrderShow
    {
        public string 姓名 { get; set; }
        public string 电话 { get; set; }
        public string 地址 { get; set; }
        public string 订单状态 { get; set; }
        public int 商品种类数 { get; set; }
        public int 商品总数 { get; set; }
        public List<CommodityOrder> commodityList { get; set; }
        public string objectId { get; set; }


        public OrderShow(Order order)
        {
            commodityList = order.commodityList;
            姓名 = order.name;
            电话 = order.phone;
            地址 = order.address;
            objectId = order.objectId;
            int status = order.status.Get();

            switch (status)
            {
                case OrderStatus.已下单:
                    订单状态 = "已下单";
                    break;
                case OrderStatus.已送达:
                    订单状态 = "已送达";
                    break;
                case OrderStatus.已确认收货:
                    订单状态 = "已确认收货";
                    break;
                case OrderStatus.已取消:
                    订单状态 = "已取消";
                    break;
                case OrderStatus.已删除:
                    订单状态 = "已删除";
                    break;
            }

            商品种类数 = 0;
            商品总数 = 0;

            var commodityOrders = order.commodityList;


            foreach (CommodityOrder commodityOrder in commodityOrders)
            {
                商品种类数++;
                商品总数 = 商品总数 + commodityOrder.count.Get();
            }
        }
    }
}