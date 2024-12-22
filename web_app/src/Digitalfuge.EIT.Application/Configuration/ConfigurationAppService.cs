using System.Threading.Tasks;
using Abp.Authorization;
using Abp.Runtime.Session;
using Digitalfuge.EIT.Configuration.Dto;

namespace Digitalfuge.EIT.Configuration
{
    [AbpAuthorize]
    public class ConfigurationAppService : EITAppServiceBase, IConfigurationAppService
    {
        public async Task ChangeUiTheme(ChangeUiThemeInput input)
        {
            await SettingManager.ChangeSettingForUserAsync(AbpSession.ToUserIdentifier(), AppSettingNames.UiTheme, input.Theme);
        }
    }
}
