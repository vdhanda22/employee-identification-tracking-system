using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Web;

namespace TrackingModule.Models
{
    public class Position
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int Id { get; set; }
        public int TagId { get; set; }
        public virtual Tag Tag { get; set; }
        public int MapId { get; set; }
        public virtual Map Map { get; set; }
        public double XPos { get; set; }
        public double YPos { get; set; }
        public DateTime Reported { get; set; }
    }
}