using Abp.Domain.Entities;
using Digitalfuge.EIT.Authorization.Users;
using Digitalfuge.EIT.Movements;
using Digitalfuge.EIT.Tags;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Digitalfuge.EIT.Employees
{
    public class Employee:Entity<long>
    {
        [ForeignKey("User")]
        public override long Id { get; set; }
        public DateTime DateOfBirth { get; set; }
        [MaxLength(200)]
        public string AddressLane1 { get; set; }
        [MaxLength(200)]
        public string AddressLane2 { get; set; }
        [MaxLength(50)]
        public string City { get; set; }
        [MaxLength(50)]
        public string State { get; set; }
        public string Country { get; set; }
        public string Image { get; set; }
        public bool IsTrained { get; set; }
        [ForeignKey("Tag")]
        public long? TagId { get; set; }
        public virtual Tag Tag { get; set; }
        public virtual User User { get; set; }
        public virtual ICollection<Location> Locations { get; set; }
        public virtual ICollection<Movement> Movements { get; set; }
    }
}
