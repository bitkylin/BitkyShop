using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using cn.bmob.io;

namespace bitkyShop.bean
{
    public class CommodityOrder : BmobTable
    {
        public String objectId;
        public BmobDouble price;
        public BmobInt count;



        //读字段信息
        public override void readFields(BmobInput input)
        {
            base.readFields(input);
            objectId = input.getString("objectId");
            price = input.getDouble("price");
            count = input.getInt("count");
        }

        //写字段信息
        public override void write(BmobOutput output, bool all)
        {
            base.write(output, all);
            output.Put("objectId", objectId);
            output.Put("price", price);
            output.Put("count", count);
        }
    }
}
