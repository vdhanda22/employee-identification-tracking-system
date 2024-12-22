using Abp.Domain.Entities;
using Digitalfuge.EIT.Rooms;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Digitalfuge.EIT.Rfids
{
    public class Rfid:Entity<long>
    {
        public string Name { get; set; }
        public string Address { get; set; }
        public string Color { get; set; }
        public long RoomId { get; set; }
        public virtual Room Room { get; set; }
    }
}
