using System.Reflection;
using System.Web;
using System.Web.Mvc;
using System.Web.Optimization;
using System.Web.Routing;
using Abp.Auditing;
using Abp.Configuration.Startup;
using Abp.Dependency;
using Abp.Hangfire;
using Abp.Hangfire.Configuration;
using Abp.IO;
using Abp.Modules;
using Abp.Web.Mvc;
using Abp.Web.SignalR;
using Abp.Zero.Configuration;
using Digitalfuge.EIT.Api;
using Hangfire;

namespace Digitalfuge.EIT.Web
{
    [DependsOn(
        typeof(EITDataModule),
        typeof(EITApplicationModule),
        typeof(EITWebApiModule),
        typeof(AbpWebSignalRModule),
        //typeof(AbpHangfireModule), - ENABLE TO USE HANGFIRE INSTEAD OF DEFAULT JOB MANAGER
        typeof(AbpWebMvcModule))]
    public class EITWebModule : AbpModule
    {
        public override void PreInitialize()
        {
            //Enable database based localization
            Configuration.Modules.Zero().LanguageManagement.EnableDbLocalization();

            //Configure navigation/menu
            Configuration.Navigation.Providers.Add<EITNavigationProvider>();

            //Configure Hangfire - ENABLE TO USE HANGFIRE INSTEAD OF DEFAULT JOB MANAGER
            //Configuration.BackgroundJobs.UseHangfire(configuration =>
            //{
            //    configuration.GlobalConfiguration.UseSqlServerStorage("Default");
            //});
        }

        public override void Initialize()
        {
            IocManager.RegisterAssemblyByConvention(Assembly.GetExecutingAssembly());

            AreaRegistration.RegisterAllAreas();
            RouteConfig.RegisterRoutes(RouteTable.Routes);
            BundleConfig.RegisterBundles(BundleTable.Bundles);
        }

        public override void PostInitialize()
        {
            var server = HttpContext.Current.Server;
            var appFolders = IocManager.Resolve<AppFolders>();

            //var filters = Configuration.Modules.AbpWebApi().HttpConfiguration.Filters;
            //var currentExceptionFilter = filters
            //    .First(h => h.Instance is AbpApiExceptionFilterAttribute).Instance;
            //filters.Remove(currentExceptionFilter);
            //filters.Add(IocManager.Resolve<AbpApiExceptionFilterAttributeCustom>());

            appFolders.SampleProfileImagesFolder = server.MapPath("~/Common/Images/SampleProfilePics");
            appFolders.TempFileDownloadFolder = server.MapPath("~/Temp/Downloads");
            appFolders.MapImages = server.MapPath("~/Temp/Maps");
            appFolders.WebLogsFolder = server.MapPath("~/App_Data/Logs");
            appFolders.RootFolder = server.MapPath("~/");
            appFolders.TenantImages = server.MapPath("~/App/images/tenantImages");

            try { DirectoryHelper.CreateIfNotExists(appFolders.TempFileDownloadFolder); } catch { }
            try { DirectoryHelper.CreateIfNotExists(appFolders.MapImages); } catch { }
        }
    }
}
