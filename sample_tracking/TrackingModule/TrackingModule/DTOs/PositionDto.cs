using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace TrackingModule.DTOs
{
    public class PositionDto
    {
        public int ReaderId { get; set; }
        public int TagId { get; set; }
        public double RSSI { get; set; }
        public int MapId { get; set; }
        public double XPos { get; set; }
        public double YPos { get; set; }
    }
}