using System.Collections.Generic;
using cn.bmob.io;

namespace bitkyShop.bean
{
    public class Commodity : BmobTable
    {
        public string Category { set; get; }
        public string CategorySub { set; get; }
        public string Name { set; get; }
        public BmobDouble Price { set; get; }
        public BmobInt Count { set; get; }
        public string Details { set; get; }
        public string Promotion { set; get; }
        public string AD { set; get; }
        public string BitkyId { set; get; }
        public BmobInt BitkyMode { set; get; }
        public string BitkyModeStr { set; get; }
        public string CoverPhotoUrl { set; get; }
        public List<string> CoverPhotoUrlSet { set; get; }
        public string CoverPhotoName { get; set; }
        public List<string> CoverPhotoNameSet { set; get; }

        public Commodity()
        {
            CategorySub = "";
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
            BitkyModeStr = input.getString("BitkyModeStr");
            Promotion = input.getString("Promotion");
            AD = input.getString("AD");
            Category = input.getString("Category");
            CategorySub = input.getString("CategorySub");
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
            output.Put("BitkyModeStr", BitkyModeStr);
            output.Put("Promotion", Promotion);
            output.Put("AD", AD);
            output.Put("Category", Category);
            output.Put("CategorySub", CategorySub);
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