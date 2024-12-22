using Abp.Application.Services;
using Abp.Application.Services.Dto;
using Digitalfuge.EIT.MultiTenancy.Dto;

namespace Digitalfuge.EIT.MultiTenancy
{
    public interface ITenantAppService : IAsyncCrudAppService<TenantDto, int, PagedResultRequestDto, CreateTenantDto, TenantDto>
    {
    }
}
