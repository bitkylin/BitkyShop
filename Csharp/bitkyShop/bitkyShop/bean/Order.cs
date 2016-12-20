using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using cn.bmob.io;

namespace bitkyShop.bean
{
    public class Order : BmobTable
    {
        public string userObjectId { set; get; }
        public string username { set; get; }
        public BmobInt status { set; get; }

        public List<CommodityOrder> commodityList { set; get; }

        public string name { set; get; }
        public string phone { set; get; }
        public string address { set; get; }

        //读字段信息
        public override void readFields(BmobInput input)
        {
            base.readFields(input);
            userObjectId = input.getString("userObjectId");
            username = input.getString("username");
            status = input.getInt("status");
            commodityList = input.getList<CommodityOrder>("commodityList");

            name = input.getString("name");
            phone = input.getString("phone");
            address = input.getString("address");
        }

        //写字段信息
        public override void write(BmobOutput output, bool all)
        {
            base.write(output, all);
            output.Put("userObjectId", userObjectId);
            output.Put("username", username);
            output.Put("status", status);
            output.Put("commodityList", commodityList);
            output.Put("name", name);
            output.Put("phone", phone);
            output.Put("address", address);
        }
    }
}