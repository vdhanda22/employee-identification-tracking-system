using Abp.Domain.Entities;
using Digitalfuge.EIT.Rooms;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Digitalfuge.EIT.Maps
{
    public class Map : Entity<long>
    {
        public string FileName { get; set; }
        public string Name { get; set; }
        public virtual ICollection<Room> Rooms { get; set; }
    }
}
