using Abp.Application.Services.Dto;
using Abp.AutoMapper;
using Digitalfuge.EIT.Movements;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Digitalfuge.EIT.Employees.Dto
{
    [AutoMapFrom(typeof(Employee))]
    public class EmployeeListDto : EntityDto<long>
    {
        public string Name { get; set; }
        public string EmailAddress { get; set; }
        public bool IsActive { get; set; }
        public bool IsTrained { get; set; }
        public string Image { get; set; }
    }
}
