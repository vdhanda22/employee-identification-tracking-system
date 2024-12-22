using Abp.Application.Services.Dto;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Web;

namespace Digitalfuge.EIT.Employees.Dto
{
    public class EditEmployeeDto 
    {
        public long? Id { get; set; }
        public string UserName { get; set; }
        public string Name { get; set; }
        public string Surname { get; set; }
        public string EmailAddress { get; set; }
        public bool IsActive { get; set; }
        public DateTime DateOfBirth { get; set; }
        public string AddressLane1 { get; set; }
        public string AddressLane2 { get; set; }
        public string City { get; set; }
        public string Password { get; set; }
        public string State { get; set; }
        public string Country { get; set; }
        public string Image { get; set; }
        public long? TagId { get; set; }
        public bool IsTrained { get; set; }
    }
}
