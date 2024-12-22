using Abp.AutoMapper;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Digitalfuge.EIT.Rooms.Dto
{
    [AutoMapTo(typeof(Room))]
    public class CreateRoomDto
    {
        public string Name { get; set; }
        public string PositionX { get; set; }
        public string PositionY { get; set; }
        public long MapId { get; set; }
    }
}
