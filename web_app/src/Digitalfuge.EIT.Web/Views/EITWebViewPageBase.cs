using Abp.Web.Mvc.Views;

namespace Digitalfuge.EIT.Web.Views
{
    public abstract class EITWebViewPageBase : EITWebViewPageBase<dynamic>
    {

    }

    public abstract class EITWebViewPageBase<TModel> : AbpWebViewPage<TModel>
    {
        protected EITWebViewPageBase()
        {
            LocalizationSourceName = EITConsts.LocalizationSourceName;
        }
    }
}