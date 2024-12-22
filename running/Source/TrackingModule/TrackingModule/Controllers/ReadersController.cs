using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Linq;
using System.Threading.Tasks;
using System.Net;
using System.Web;
using System.Web.Mvc;
using TrackingModule.Models;

namespace TrackingModule.Controllers
{
    public class ReadersController : Controller
    {
        private ApplicationDbContext db = new ApplicationDbContext();

        // GET: Readers
        public async Task<ActionResult> Index()
        {
            var readers = db.Readers.Include(r => r.Map);
            return View(await readers.ToListAsync());
        }

        // GET: Readers/Details/5
        public async Task<ActionResult> Details(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            Reader reader = await db.Readers.FindAsync(id);
            if (reader == null)
            {
                return HttpNotFound();
            }
            return View(reader);
        }

        // GET: Readers/Create
        public ActionResult Create()
        {
            ViewBag.MapId = new SelectList(db.Maps, "Id", "MapFile");
            return View();
        }

        // POST: Readers/Create
        // To protect from overposting attacks, enable the specific properties you want to bind to, for 
        // more details see https://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<ActionResult> Create([Bind(Include = "Id,Name,NodeAddress,MapId,XPos,YPos")] Reader reader)
        {
            if (ModelState.IsValid)
            {
                db.Readers.Add(reader);
                await db.SaveChangesAsync();
                return RedirectToAction("Index");
            }

            ViewBag.MapId = new SelectList(db.Maps, "Id", "MapFile", reader.MapId);
            return View(reader);
        }

        // GET: Readers/Edit/5
        public async Task<ActionResult> Edit(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            Reader reader = await db.Readers.FindAsync(id);
            if (reader == null)
            {
                return HttpNotFound();
            }
            ViewBag.MapId = new SelectList(db.Maps, "Id", "MapFile", reader.MapId);
            return View(reader);
        }

        // POST: Readers/Edit/5
        // To protect from overposting attacks, enable the specific properties you want to bind to, for 
        // more details see https://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<ActionResult> Edit([Bind(Include = "Id,Name,NodeAddress,MapId,XPos,YPos")] Reader reader)
        {
            if (ModelState.IsValid)
            {
                db.Entry(reader).State = EntityState.Modified;
                await db.SaveChangesAsync();
                return RedirectToAction("Index");
            }
            ViewBag.MapId = new SelectList(db.Maps, "Id", "MapFile", reader.MapId);
            return View(reader);
        }

        // GET: Readers/Delete/5
        public async Task<ActionResult> Delete(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            Reader reader = await db.Readers.FindAsync(id);
            if (reader == null)
            {
                return HttpNotFound();
            }
            return View(reader);
        }

        // POST: Readers/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<ActionResult> DeleteConfirmed(int id)
        {
            Reader reader = await db.Readers.FindAsync(id);
            db.Readers.Remove(reader);
            await db.SaveChangesAsync();
            return RedirectToAction("Index");
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }
    }
}
