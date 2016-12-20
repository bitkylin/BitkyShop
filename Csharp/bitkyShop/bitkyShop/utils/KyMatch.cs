using System.Text.RegularExpressions;

namespace bitkyShop.utils
{
    public class KyMatch
    {
        /// <summary>
        ///     判断是自然数
        /// </summary>
        /// <param name="value">待匹配的文本</param>
        /// <returns>匹配结果</returns>
        public static bool IsInt(string value) //判断是自然数
        {
            value = value.Trim();
            return Regex.IsMatch(value, @"^[1-9]\d*|0$");
        }

        /// <summary>
        ///     判断是正浮点数
        /// </summary>
        /// <param name="value">待匹配的文本</param>
        /// <returns>匹配结果</returns>
        public static bool IsFloat(string value) //判断是正浮点数或自然数
        {
            value = value.Trim();
            return Regex.IsMatch(value, @"^[1-9]d*.d*|0.d*[1-9]d*|[1-9]\d*|0$");
        }
    }
}