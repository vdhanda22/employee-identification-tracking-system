using Digitalfuge.EIT.Movements.Dto;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Digitalfuge.EIT.Api.Models
{
    public class RfidLocationModel
    {
        public List<TagCollection> Data { get; set; }
    }
}
