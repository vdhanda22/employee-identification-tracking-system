using System.Threading.Tasks;
using Abp.Application.Services;
using Digitalfuge.EIT.Configuration.Dto;

namespace Digitalfuge.EIT.Configuration
{
    public interface IConfigurationAppService: IApplicationService
    {
        Task ChangeUiTheme(ChangeUiThemeInput input);
    }
}