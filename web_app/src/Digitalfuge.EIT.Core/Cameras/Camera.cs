using Abp.Domain.Entities;
using Digitalfuge.EIT.Rooms;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Digitalfuge.EIT.Cameras
{
    public class Camera : Entity<long>
    {
        public string Name { get; set; }
        public string Url { get; set; }
        public string ProcessId { get; set; }
        public string IsRunning { get; set; }
        [ForeignKey("Room")]
        public long RoomId { get; set; }
        public virtual Room Room { get; set; }
    }
}
