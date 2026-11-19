# Generate PDF from the README using fpdf2
from fpdf import FPDF
from fpdf.html import HTML2FPDF
import re

class ReportPDF(FPDF):
    def header(self):
        if self.page_no() > 1:
            self.set_font('Inter', 'I', 8)
            self.set_text_color(100, 100, 100)
            self.cell(0, 5, 'KampusCare - UTS PM | Aditya Febrian | 240104040129', align='C')
            self.ln(4)
            self.set_draw_color(180, 180, 180)
            self.line(self.get_x(), self.get_y(), self.w - 20, self.get_y())
            self.ln(6)

    def footer(self):
        self.set_y(-15)
        self.set_font('Inter', 'I', 8)
        self.set_text_color(130, 130, 130)
        self.cell(0, 10, f'Halaman {self.page_no()} / {self.pages_count}', align='C')


pdf = ReportPDF()
pdf.set_auto_page_break(auto=True, margin=20)
pdf.add_font('Inter', '', 'D:/Coolyeah/UTS_PM/output/Inter-Regular.ttf')
pdf.add_font('Inter', 'B', 'D:/Coolyeah/UTS_PM/output/Inter-Bold.ttf')
pdf.add_font('Inter', 'I', 'D:/Coolyeah/UTS_PM/output/Inter-Regular.ttf')
pdf.add_page()

# Read the README file
with open('D:/Coolyeah/UTS_PM/output/README.md', 'r', encoding='utf-8') as f:
    content = f.read()

# Convert markdown to HTML
def md_to_html(text):
    # Headers
    text = re.sub(r'^# (.+)$', r'<h1 style="font-size:18pt;font-weight:bold;text-align:center;color:#00695C;">\1</h1>', text, flags=re.MULTILINE)
    text = re.sub(r'^## (.+)$', r'<h2 style="font-size:14pt;font-weight:bold;color:#004D40;">\1</h2>', text, flags=re.MULTILINE)
    text = re.sub(r'^### (.+)$', r'<h3 style="font-size:12pt;font-weight:bold;color:#00695C;">\1</h3>', text, flags=re.MULTILINE)
    # Bold
    text = re.sub(r'\*\*(.+?)\*\*', r'<strong style="font-weight:bold;">\1</strong>', text)
    # Italic
    text = re.sub(r'\*(.+?)\*', r'<em>\1</em>', text)
    # Horizontal rules
    text = re.sub(r'^---$', r'<hr>', text, flags=re.MULTILINE)
    # List items
    text = re.sub(r'^(\s*[-*+])\s+(.+)$', r'<li>\2</li>', text, flags=re.MULTILINE)
    # Paragraphs - split by double newline
    text = re.sub(r'\n{3,}', '</p><p style="text-align:justify;">', text)
    text = re.sub(r'^', '<p>', text)
    text = re.sub(r'$', '</p>', text)
    return text

html_content = md_to_html(content)

pdf.alias_nb_pages()
pdf.write_html(html_content, li_tag_indent=8, font_family='Inter', heading_sizes={'h1': 18, 'h2': 14, 'h3': 12, 'h4': 11}, pre_code_font='Inter', table_line_separators=True)

output_path = 'D:/Coolyeah/UTS_PM/output/Laporan_KampusCare.pdf'
pdf.output(output_path)
print(f'PDF generated: {output_path}')
print(f'Pages: {pdf.pages_count}')
