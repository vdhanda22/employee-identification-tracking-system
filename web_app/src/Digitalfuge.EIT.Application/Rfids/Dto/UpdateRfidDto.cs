using Abp.Application.Services.Dto;
using Abp.AutoMapper;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Digitalfuge.EIT.Rfids.Dto
{
    [AutoMapTo(typeof(Rfid))]
    public class UpdateRfidDto : EntityDto<long>
    {
        public string Name { get; set; }
        public string Address { get; set; }
        public string Color { get; set; }
        public long RoomId { get; set; }
    }
}
