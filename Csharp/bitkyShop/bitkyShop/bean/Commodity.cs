using System.Collections.Generic;
using cn.bmob.io;

namespace bitkyShop.bean
{
    public class Commodity : BmobTable
    {
        public string BitkyId { set; get; }
        public BmobInt BitkyMode { set; get; }
        public string BitkyModeStr { set; get; }
        public string Category { set; get; }
        public string Name { set; get; }
        public BmobDouble Price { set; get; }
        public BmobInt Count { set; get; }
        public string Details { set; get; }
        public string CoverPhotoUrl { set; get; }
        public List<string> CoverPhotoUrlSet { set; get; }
        public string CoverPhotoName { get; set; }
        public List<string> CoverPhotoNameSet { set; get; }

        public Commodity()
        {
            BitkyModeStr = "none";
            BitkyId = "default";
            BitkyMode = 0;
            CoverPhotoUrlSet = new List<string>();
            CoverPhotoNameSet = new List<string>();
        }

        public Commodity SetStamp(string msg)
        {
            BitkyId = msg;
            return this;
        }

        public Commodity SetMode(int msg)
        {
            BitkyMode = msg;
            return this;
        }

        //读字段信息
        public override void readFields(BmobInput input)
        {
            base.readFields(input);

            BitkyId = input.getString("BitkyId");
            BitkyMode = input.getInt("BitkyMode");
            Category = input.getString("Category");
            Name = input.getString("Name");
            Price = input.getDouble("Price");
            Count = input.getInt("Count");
            Details = input.getString("Details");
            CoverPhotoName = input.getString("CoverPhotoName");
            CoverPhotoUrl = input.getString("CoverPhotoUrl");
            CoverPhotoNameSet = input.getList<string>("CoverPhotoNameSet");
            CoverPhotoUrlSet = input.getList<string>("CoverPhotoUrlSet");
        }

        //写字段信息
        public override void write(BmobOutput output, bool all)
        {
            base.write(output, all);

            output.Put("BitkyId", BitkyId);
            output.Put("BitkyMode", BitkyMode);
            output.Put("Category", Category);
            output.Put("Name", Name);
            output.Put("Price", Price);
            output.Put("Count", Count);
            output.Put("Details", Details);
            output.Put("CoverPhotoName", CoverPhotoName);
            output.Put("CoverPhotoUrl", CoverPhotoUrl);
            output.Put("CoverPhotoNameSet", CoverPhotoNameSet);
            output.Put("CoverPhotoUrlSet", CoverPhotoUrlSet);
        }
    }
}