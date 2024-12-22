using Abp.AutoMapper;
using Digitalfuge.EIT.Sessions.Dto;

namespace Digitalfuge.EIT.Web.Models.Account
{
    [AutoMapFrom(typeof(GetCurrentLoginInformationsOutput))]
    public class TenantChangeViewModel
    {
        public TenantLoginInfoDto Tenant { get; set; }
    }
}