using Qiniu.Http;

namespace bitkyShop.view
{
    public interface ICloudServiceView
    {
        /// <summary>
        ///  七牛上传完毕事件处理
        /// </summary>
        /// <param name="key">已上传的文件名</param>
        /// <param name="respInfo">返回信息</param>
        /// <param name="respJson">返回Json</param>
        void OnQiniuUploadCompleted(string key, ResponseInfo respInfo, string respJson);

        void ConfirmUpload();
        /// <summary>
        /// Bmob上传完毕回调
        /// </summary>
        /// <param name="code">错误代码，0:成功，1:失败</param>
        /// <param name="msg">返回信息</param>
        void OnBmobUploadCompleted(int code, string msg);
    }
}