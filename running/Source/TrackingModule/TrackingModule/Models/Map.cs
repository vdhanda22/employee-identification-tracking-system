using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Web;

namespace TrackingModule.Models
{
    public class Map
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int Id { get; set; }
        public string MapFile { get; set; }
        public virtual ICollection<Reader> Readers { get; set; }
        public virtual ICollection<Tag> Tags { get; set; }
        public virtual ICollection<Position> Positions { get; set; }
    }
}