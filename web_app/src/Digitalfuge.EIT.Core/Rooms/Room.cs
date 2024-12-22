using Abp.Domain.Entities;
using Digitalfuge.EIT.Cameras;
using Digitalfuge.EIT.Maps;
using Digitalfuge.EIT.Movements;
using Digitalfuge.EIT.Rfids;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Digitalfuge.EIT.Rooms
{
    public class Room : Entity<long>
    {
        public string Name { get; set; }
        public string PositionX { get; set; }
        public string PositionY { get; set; }
        public long MapId { get; set; }
        public virtual Map Map { get; set; }
        public virtual ICollection<Camera> Cameras { get; set; }
        public virtual ICollection<Rfid> Rfids { get; set; }

        public virtual ICollection<Location> Locations { get; set; }
    }
}
