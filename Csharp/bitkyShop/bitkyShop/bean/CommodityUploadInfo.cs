using System;
using Qiniu.Storage;

namespace bitkyShop.bean
{
    internal class CommodityUploadInfo
    {
        private static CommodityUploadInfo Info = null;

        private CommodityUploadInfo(string uploadToken, UploadOptions uploadOptions, UpCompletionHandler uploadCompleted)
        {
            UploadToken = uploadToken;
            UploadOptions = uploadOptions;
            UploadCompleted = uploadCompleted;
        }

        public static CommodityUploadInfo Builder(string uploadToken, UploadOptions uploadOptions,
            UpCompletionHandler uploadCompleted)
        {
            Info = Info == null ? new CommodityUploadInfo(uploadToken, uploadOptions, uploadCompleted) : Info;
            return Info;
        }

        public CommodityUploadInfo SetQiniuFilePath(string filePath)
        {
            if (Info == null) throw new NoThisInstanceException("没有实例化");
            Info.FilePath = filePath;
            return this;
        }

        public CommodityUploadInfo SetQiniuFileKey(string fileKey)
        {
            if (Info == null) throw new NoThisInstanceException("没有实例化");
            Info.FileKey = fileKey;
            return this;
        }

        public string FilePath { get; set; }
        public string FileKey { get; set; }
        public string UploadToken { get; set; }
        public UploadOptions UploadOptions { get; set; }
        public UpCompletionHandler UploadCompleted { get; set; }
    }

    internal class NoThisInstanceException : Exception
    {
        public NoThisInstanceException(string msg) : base(msg)
        {
        }
    }
}