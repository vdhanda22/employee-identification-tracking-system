using Abp.Application.Services.Dto;
using Abp.AutoMapper;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Digitalfuge.EIT.Cameras.Dto
{
    [AutoMapTo(typeof(Camera))]
    public class UpdateCameraDto : EntityDto<long>
    {
        public string Name { get; set; }
        public string Url { get; set; }
        public string ProcessId { get; set; }
        public string IsRunning { get; set; }
        public long RoomId { get; set; }
    }
}
