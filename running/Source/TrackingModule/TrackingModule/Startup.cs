using Microsoft.Owin;
using Owin;

[assembly: OwinStartupAttribute(typeof(TrackingModule.Startup))]
namespace TrackingModule
{
    public partial class Startup
    {
        public void Configuration(IAppBuilder app)
        {
            ConfigureAuth(app);
        }
    }
}
