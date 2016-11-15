using System.Diagnostics;
using System.IO;
using System.Threading;
using bitkyShop.bean;
using bitkyShop.utils;
using cn.bmob.api;
using cn.bmob.tools;
using Qiniu.Http;
using Qiniu.Storage;
using Qiniu.Util;

namespace bitkyShop.init
{
    class CloudServiceHelper
    {
        private CommPresenter _presenter;
        private string _filePath = string.Empty;
        private string _fileKey = string.Empty;
        private string _uploadToken = string.Empty;
        private UploadOptions _uploadOptions = null;
        private UpCompletionHandler _uploadCompleted;
        private BmobWindows _bmobWindows;


        public CloudServiceHelper(CommPresenter presenter)
        {
            _presenter = presenter;
            PathInit();
            BmobBuilder();
            QiniuInit();
        }

        /// <summary>
        /// 初始化图片缓存路径
        /// </summary>
        private void PathInit()
        {
            if (!Directory.Exists("./photoCache"))
            {
                Directory.CreateDirectory("./photoCache");
            }
        }

        /// <summary>
        /// Bmob初始化
        /// </summary>
        public BmobWindows BmobBuilder()
        {
            if (_bmobWindows == null)
            {
                _bmobWindows = new BmobWindows();
                _bmobWindows.initialize(PresetInfo.BmobApplicationId, PresetInfo.BmobRestApiKey);
                BmobDebug.Register(msg => { Debug.WriteLine("BmobDebug:" + msg); });
            }
            return _bmobWindows;
        }



        /// <summary>
        /// 七牛初始化
        /// </summary>
        private void QiniuInit()
        {
            const string bucket = "bitkyshop";
            var policy = new PutPolicy(); // 上传策略实例
            policy.Scope = bucket; // 设置要上传的目标空间
            policy.SetExpires(86400); // 上传策略的过期时间(单位:秒)，当前设为24小时
            var zoneId = Qiniu.Common.AutoZone.Query(PresetInfo.QiniuAccessKey, bucket); // 这里的Zone设置(如果不设置，就默认为华东机房)
            Qiniu.Common.Config.ConfigZone(zoneId);
            var mac = new Mac(PresetInfo.QiniuAccessKey, PresetInfo.QiniuSecretKey);
            _uploadToken = Auth.createUploadToken(policy, mac); // 生成上传凭证

            // 上传完毕事件处理
            _uploadCompleted = new UpCompletionHandler(OnQiniuUploadCompleted);
        }

        /// <summary>
        ///  七牛上传完毕事件处理
        /// </summary>
        /// <param name="key">已上传的文件名</param>
        /// <param name="respInfo">返回信息</param>
        /// <param name="respJson">返回Json</param>
        public void OnQiniuUploadCompleted(string key, ResponseInfo respInfo, string respJson)
        {
            _presenter.OnQiniuUploadCompleted(key, respInfo, respJson);
        }

        /// <summary>
        /// 异步执行：七牛上传文件
        /// </summary>
        /// <param name="filePath">输入文件地址</param>
        public void UploadFile(string filePath)
        {
            _filePath = filePath;
            _fileKey = HashHelper.ComputeMd5(filePath);
            var info = CommodityUploadInfo
                .Builder(_uploadToken, _uploadOptions, _uploadCompleted)
                .SetQiniuFilePath(_filePath)
                .SetQiniuFileKey(_fileKey);
            new Thread(QiniuUploadFile).Start(info);
        }

        /// <summary>
        /// 异步执行：七牛上传文件
        /// </summary>
        /// <param name="data"></param>
        private static void QiniuUploadFile(object data)
        {
            var info = (CommodityUploadInfo) data;
            var um = new UploadManager();
            um.uploadFile(info.FilePath, info.FileKey, info.UploadToken, info.UploadOptions, info.UploadCompleted);
        }
    }
}