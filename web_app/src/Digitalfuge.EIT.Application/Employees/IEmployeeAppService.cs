using Abp.Application.Services;
using Abp.Application.Services.Dto;
using Digitalfuge.EIT.Employees.Dto;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Digitalfuge.EIT.Employees
{
    public interface IEmployeeAppService: IApplicationService
    {
        ListResultDto<EmployeeListDto> GetEmployees(GetEmployeesInput input);
        Task<EditEmployeeDto> GetEmployeeForEdit(NullableIdDto<long> input);
        Task CreateOrUpdateEmployeeAsync(CreateOrUpdateEmployeeInput input);
        Task DeleteEmployee(long id);
        Task TrainImage(string imagePath, long employeeId);
    }
}
