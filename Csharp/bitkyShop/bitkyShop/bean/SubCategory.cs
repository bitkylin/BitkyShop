using System.Collections.Generic;
using cn.bmob.io;

namespace bitkyShop.bean
{
    public class SubCategory : BmobTable
    {
        public string mainCategory { set; get; }
        public string photoUrl { set; get; }
        public string photoName { set; get; }
        public string name { set; get; }
        //读字段信息
        public override void readFields(BmobInput input)
        {
            base.readFields(input);
            mainCategory = input.getString("mainCategory");
            photoUrl = input.getString("photoUrl");
            photoName = input.getString("photoName");
            name = input.getString("name");
        }

        //写字段信息
        public override void write(BmobOutput output, bool all)
        {
            base.write(output, all);
            output.Put("mainCategory", mainCategory);
            output.Put("photoUrl", photoUrl);
            output.Put("photoName", photoName);
            output.Put("name", name);
        }
    }
}