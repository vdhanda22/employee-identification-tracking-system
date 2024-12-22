using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using TrackingModule.Models;

namespace TrackingModule.DTOs
{
    public class LiveMapDto
    {
        public List<Tag> Tags { get; set; }
        public List<Reader> Readers { get; set; }
        public Map Map { get; set; }
    }
}