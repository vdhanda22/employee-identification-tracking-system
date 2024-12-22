using System.Threading.Tasks;
using Abp.Application.Services;
using Digitalfuge.EIT.Sessions.Dto;

namespace Digitalfuge.EIT.Sessions
{
    public interface ISessionAppService : IApplicationService
    {
        Task<GetCurrentLoginInformationsOutput> GetCurrentLoginInformations();
    }
}
