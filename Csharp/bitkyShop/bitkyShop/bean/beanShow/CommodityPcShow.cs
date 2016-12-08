using System.Collections.Generic;
using cn.bmob.io;

namespace bitkyShop.bean.beanShow
{
    public class CommodityPcShow
    {
        public string objectId { set; get; }
        public string 分类 { set; get; }
        public string 子类 { set; get; }
        public string 名称 { set; get; }
        public BmobDouble 价格 { set; get; }
        public BmobInt 数量 { set; get; }
        public string 详细介绍 { set; get; }
        public string 促销状态 { set; get; }
        public string 轮播广告状态 { set; get; }
        public CommodityPcShow(string objectId, string category, string categorySub, string name, BmobDouble price, BmobInt count, string details, string promotion, string ad)
        {
            this.objectId = objectId;
            分类 = category;
            子类 = categorySub;
            名称 = name;
            价格 = price;
            数量 = count;
            详细介绍 = details;
            促销状态 = promotion;
            轮播广告状态 = ad;
        }

    }
}