using Abp.Application.Services;
using Abp.Application.Services.Dto;
using Abp.Authorization.Users;
using Abp.Collections.Extensions;
using Abp.Domain.Repositories;
using Abp.Extensions;
using Abp.UI;
using Digitalfuge.EIT.Authorization.Roles;
using Digitalfuge.EIT.Authorization.Users;
using Digitalfuge.EIT.Employees.Dto;
using Microsoft.AspNet.Identity;
using RestSharp;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Digitalfuge.EIT.Employees
{
    public class EmployeeAppService : EITAppServiceBase, IEmployeeAppService
    {
        private readonly IRepository<Employee,long> _employeeRepository;
        private readonly UserManager _userManager;
        private readonly RoleManager _roleManager;
        private readonly IRepository<Role> _roleRepository;
        public EmployeeAppService(
            IRepository<Employee, long> employeeRepository,
             UserManager userManager,
            IRepository<Role> roleRepository,
            RoleManager roleManager)
        {
            _employeeRepository = employeeRepository;
            _userManager = userManager;
            _roleRepository = roleRepository;
            _roleManager = roleManager;
        }
        public async Task UpdateEmployee(EditEmployeeDto input)
        {
            var user = _userManager.GetUserById(input.Id.Value);
            user.UserName = input.UserName;
            user.EmailAddress = input.EmailAddress;
            user.Name = input.Name;
            user.Surname = input.Surname;
            user.IsActive = input.IsActive;
            _userManager.Update(user);
            
            var employee = await _employeeRepository.GetAsync(input.Id.Value);
            var tempImage = employee.Image;
            employee.AddressLane1 = input.AddressLane1;
            employee.AddressLane2 = input.AddressLane2;
            employee.City = input.City;
            employee.Country = input.City;
            employee.DateOfBirth = input.DateOfBirth;
            employee.IsTrained = string.IsNullOrEmpty(input.Image) ? employee.IsTrained : (input.Image == employee.Image ? employee.IsTrained : false);
            employee.Image = string.IsNullOrEmpty(input.Image) ? employee.Image : input.Image;            
            employee.State = input.State;
            employee.TagId = input.TagId;
            _employeeRepository.Update(employee);
            await CurrentUnitOfWork.SaveChangesAsync();

            if (!string.IsNullOrEmpty(input.Image)&& input.Image!= tempImage)
            {
                await TrainImage(@"C:\Data\6.3.0\src\Digitalfuge.EIT.Web\Temp\Downloads\" + input.Image, employee.Id);
            }
        }
        public async Task CreateEmployee(EditEmployeeDto input)
        {
            var user = new User()
            {
                UserName = input.UserName,
                EmailAddress = input.EmailAddress,
                Name = input.Name,
                Surname = input.Surname,
                IsActive = input.IsActive
            };

            user.TenantId = AbpSession.TenantId;
            user.Password = new PasswordHasher().HashPassword(input.Password);
            user.IsEmailConfirmed = true;

            //Assign roles
            user.Roles = new Collection<UserRole>();
            var role = await _roleManager.GetRoleByNameAsync("Employee");
            user.Roles.Add(new UserRole(AbpSession.TenantId, user.Id, role.Id));
            CheckErrors(await _userManager.CreateAsync(user));

            await CurrentUnitOfWork.SaveChangesAsync();

            var employee = new Employee()
            {
                Id = user.Id,
                AddressLane1 = input.AddressLane1,
                AddressLane2 = input.AddressLane2,
                City = input.City,
                Country = input.City,
                DateOfBirth = input.DateOfBirth,
                Image = string.IsNullOrEmpty(input.Image)? "":input.Image,
                IsTrained = false,
                State = input.State,
                TagId= input.TagId
            };
            await _employeeRepository.InsertAsync(employee);
            await CurrentUnitOfWork.SaveChangesAsync();
            if (!string.IsNullOrEmpty(input.Image))
            {
                await TrainImage(@"C:\Data\6.3.0\src\Digitalfuge.EIT.Web\Temp\Downloads\" + input.Image, employee.Id);
            }

        }

        public async Task TrainImage(string imagePath, long employeeId)
        {
            try
            {
                var client = new RestClient();
                var request = new RestRequest("http://localhost:5002/api/v1/upload", Method.POST);

                request.AddFile("file", imagePath);
                request.AddParameter("employeeId", employeeId);
                request.AlwaysMultipartFormData = true;

                var response = await client.ExecuteAsync(request);
                if (response.StatusCode == System.Net.HttpStatusCode.OK)
                {
                    var employee=await _employeeRepository.GetAsync(employeeId);
                    employee.IsTrained = true;
                    _employeeRepository.Update(employee);
                    await CurrentUnitOfWork.SaveChangesAsync();
                }
            }catch(Exception e)
            {
                throw new UserFriendlyException(e.Message);
            }
         
        }
        public async Task CreateOrUpdateEmployeeAsync(CreateOrUpdateEmployeeInput input)
        {
            if (!input.Employee.Id.HasValue)
            {
                await CreateEmployee(input.Employee);
            }
            else if (input.Employee.Id.Value <= 0)
            {
                await CreateEmployee(input.Employee);
            }
            else
            {
                await UpdateEmployee(input.Employee);
            }
        }

        public async Task<EditEmployeeDto> GetEmployeeForEdit(NullableIdDto<long> input)
        {
            if (input.Id.HasValue)
            {
                var employee = await _employeeRepository.GetAsync(input.Id.Value);
                var user = _userManager.GetUserById(input.Id.Value);
                var employeeDto = new EditEmployeeDto()
                {
                    Id = input.Id.Value,
                    AddressLane1 = employee.AddressLane1,
                    AddressLane2 = employee.AddressLane2,
                    City = employee.City,
                    Country = employee.City,
                    DateOfBirth = employee.DateOfBirth,
                    State = employee.State,
                    Image = employee.Image,
                    IsTrained = employee.IsTrained,
                    EmailAddress=user.EmailAddress,
                    IsActive=user.IsActive,
                    Name=user.Name,
                    Surname=user.Surname,
                    UserName=user.UserName,
                    TagId= employee.TagId
                };
                return employeeDto;               

            }
            else
            {
                var employeeDto = new EditEmployeeDto()
                {
                    Id = null,
                    AddressLane1="",
                    AddressLane2="",
                    City="",
                    Country="",
                    DateOfBirth=System.DateTime.Now,
                    EmailAddress="",
                    Image="",
                    IsActive=false,
                    IsTrained=false,
                    Name="",
                    State="",
                    Surname="",
                    UserName=""
                };
                return employeeDto;
            }
        }

        public ListResultDto<EmployeeListDto> GetEmployees(GetEmployeesInput input)
        {
            var employees = _employeeRepository
             .GetAll()
             .WhereIf(
                 !input.Filter.IsNullOrEmpty(),
                 p => p.User.FullName.Contains(input.Filter) ||
                         p.User.Surname.Contains(input.Filter) ||
                         p.User.EmailAddress.Contains(input.Filter)
             )
             .OrderBy(p => p.User.FullName)
             .ThenBy(p => p.User.Surname)
             .Select(x=> new EmployeeListDto()
             {
                 EmailAddress=x.User.EmailAddress,
                 Id=x.Id,
                 IsActive=x.User.IsActive,
                 Name=x.User.Name,
                 IsTrained= x.IsTrained,
                 Image=x.Image
             })
             .ToList();
            return new ListResultDto<EmployeeListDto>(employees);
        }

        public async Task DeleteEmployee(long id)
        {
            try
            {
                var employee = _employeeRepository.Get(id);
                if (employee == null)
                    throw new UserFriendlyException("No Employee With this Id");
                _employeeRepository.Delete(employee);
                var user = _userManager.GetUserById(id);
                _userManager.Delete(user);
                await CurrentUnitOfWork.SaveChangesAsync();
            }catch(Exception e)
            {
                throw new UserFriendlyException(e.Message);
            }
            
        }
    }
}
