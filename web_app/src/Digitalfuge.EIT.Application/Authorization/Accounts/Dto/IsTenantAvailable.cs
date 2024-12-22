using System.ComponentModel.DataAnnotations;
using Abp.MultiTenancy;

namespace Digitalfuge.EIT.Authorization.Accounts.Dto
{
    public class IsTenantAvailableInput
    {
        [Required]
        [MaxLength(AbpTenantBase.MaxTenancyNameLength)]
        public string TenancyName { get; set; }
    }
}
