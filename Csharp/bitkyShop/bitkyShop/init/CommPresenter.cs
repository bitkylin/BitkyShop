using System.Diagnostics;
using bitkyShop.view;
using cn.bmob.api;
using cn.bmob.io;
using Qiniu.Http;

namespace bitkyShop.init
{
    public class CommPresenter
    {
        private readonly ICloudServiceView _view;
        private readonly CloudServiceHelper _cloudServiceHelper;
        private readonly BmobWindows _bmobWindows;

        public CommPresenter(ICloudServiceView view)
        {
            _view = view;
            _cloudServiceHelper = new CloudServiceHelper(this);
            _bmobWindows = _cloudServiceHelper.BmobBuilder();
        }

        public void UploadFile(string filePath)
        {
            _cloudServiceHelper.UploadFile(filePath);
        }

        public void OnQiniuUploadCompleted(string key, ResponseInfo respInfo, string respJson)
        {
            _view.OnQiniuUploadCompleted(key, respInfo, respJson);
        }

        public void UploadCommodityOrCategoryData(BmobTable data)
        {
            var fature = _bmobWindows.CreateTaskAsync(data);
            if (fature.Exception == null)
            {
                Debug.WriteLine("上传成功，createdAt:" + fature.Result.createdAt + ".objectId:" + fature.Result.objectId);
                _view.OnBmobUploadCompleted(0, fature.Result.objectId);
            }
            else
            {
                Debug.WriteLine("上传失败:" + fature.Exception.Message + ";" + fature.Exception.HResult);
                _view.OnBmobUploadCompleted(1, fature.Exception.Message);
            }
        }
    }
}