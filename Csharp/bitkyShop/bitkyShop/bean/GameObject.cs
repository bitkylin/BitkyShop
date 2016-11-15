using cn.bmob.io;

namespace bitkyShop.bean
{
    public class GameObject : BmobTable
    {
        private string fTable;
        //以下对应云端字段名称
        public BmobInt score { get; set; }
        public string playerName { get; set; }
        public BmobBoolean cheatMode { get; set; }

        //构造函数
        public GameObject()
        {
        }

        //构造函数
        public GameObject(string tableName)
        {
            this.fTable = tableName;
        }

        public override string table
        {
            get
            {
                if (fTable != null)
                {
                    return fTable;
                }
                return base.table;
            }
        }

        //读字段信息
        public override void readFields(BmobInput input)
        {
            base.readFields(input);

            this.score = input.getInt("score");
            this.cheatMode = input.getBoolean("cheatMode");
            this.playerName = input.getString("playerName");
        }

        //写字段信息
        public override void write(BmobOutput output, bool all)
        {
            base.write(output, all);

            output.Put("score", this.score);
            output.Put("cheatMode", this.cheatMode);
            output.Put("playerName", this.playerName);
        }
    }
}