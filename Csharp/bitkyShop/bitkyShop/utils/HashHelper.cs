using System.Text;

namespace bitkyShop.utils
{
    /// <summary>
    /// 提供用于计算指定文件哈希值的方法
    /// <example>例如计算文件的MD5值:
    /// <code>
    ///   String hashMd5=HashHelper.ComputeMD5("MyFile.txt");
    /// </code>
    /// </example>
    /// <example>例如计算文件的CRC32值:
    /// <code>
    ///   String hashCrc32 = HashHelper.ComputeCRC32("MyFile.txt");
    /// </code>
    /// </example>
    /// <example>例如计算文件的SHA1值:
    /// <code>
    ///   String hashSha1 =HashHelper.ComputeSHA1("MyFile.txt");
    /// </code>
    /// </example>
    /// </summary>
    public sealed class HashHelper
    {
        /// <summary>
        ///  计算指定文件的MD5值
        /// </summary>
        /// <param name="fileName">指定文件的完全限定名称</param>
        /// <returns>返回值的字符串形式</returns>
        public static string ComputeMd5(string fileName)
        {
            string hashMd5 = string.Empty;
            //检查文件是否存在，如果文件存在则进行计算，否则返回空值
            if (System.IO.File.Exists(fileName))
            {
                using (
                    var fs = new System.IO.FileStream(fileName, System.IO.FileMode.Open, System.IO.FileAccess.Read))
                {
                    //计算文件的MD5值
                    var calculator = System.Security.Cryptography.MD5.Create();
                    var buffer = calculator.ComputeHash(fs);
                    calculator.Clear();
                    //将字节数组转换成十六进制的字符串形式
                    var stringBuilder = new StringBuilder();
                    foreach (var t in buffer)
                    {
                        stringBuilder.Append(t.ToString("x2"));
                    }
                    hashMd5 = stringBuilder.ToString();
                } //关闭文件流
            } //结束计算
            return hashMd5;
        } //ComputeMD5

        /// <summary>
        ///  计算指定文件的SHA1值
        /// </summary>
        /// <param name="fileName">指定文件的完全限定名称</param>
        /// <returns>返回值的字符串形式</returns>
        public static string ComputeSha1(string fileName)
        {
            var hashSha1 = string.Empty;
            //检查文件是否存在，如果文件存在则进行计算，否则返回空值
            if (System.IO.File.Exists(fileName))
            {
                using (
                    var fs = new System.IO.FileStream(fileName, System.IO.FileMode.Open, System.IO.FileAccess.Read))
                {
                    //计算文件的SHA1值
                    var calculator = System.Security.Cryptography.SHA1.Create();
                    var buffer = calculator.ComputeHash(fs);
                    calculator.Clear();
                    //将字节数组转换成十六进制的字符串形式
                    var stringBuilder = new StringBuilder();
                    foreach (var t in buffer)
                    {
                        stringBuilder.Append(t.ToString("x2"));
                    }
                    hashSha1 = stringBuilder.ToString();
                } //关闭文件流
            }
            return hashSha1;
        } //ComputeSHA1
    } //end class: HashHelper
}