using Digitalfuge.EIT.Movements.Dto;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Digitalfuge.EIT.Movements
{
    public interface ILocationManager
    {
        Task RecieveRfidData(List<TagCollection> input);
        void RecieveCctvData(CameraDataInputRequestDto input);
    }
}
