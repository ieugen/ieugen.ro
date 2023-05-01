(require 'ox-publish)
(require 'ol)

(org-link-set-parameters "tel"
                         :export #'org-tel-export)

(defun org-tel-export (link description format _)
  "Export a tel link from Org files."
  (let ((path link)
        (desc (or description link)))
    (pcase format
      (`html (format "<a class=\"org-tel-href\" href=\"tel:%s\">%s</a>" path desc))
      (`latex (format "\\href{%s}{%s}" path desc))
      (`texinfo (format "@uref{%s,%s}" path desc))
      (`ascii (format "%s (%s)" desc path))
      (_ path))))

;; https://orgmode.org/manual/Headlines-in-HTML-export.html
(setq org-html-self-link-headlines t)

;; Define the publishing project
(setq org-publish-project-alist
      `(("nunta-org-site"
         :recursive t
         :base-directory "./content/si-Bianca-se-iau"
         :publishing-directory "./public/si-Bianca-se-iau"
         :publishing-function org-html-publish-to-html
         ; HTML5
         :html-doctype "html5"
         :html-html5-fancy t

         ; Disable some Org's HTML defaults
         :html-head-include-scripts nil
         :html-head-include-default-style nil

         :html-head "<link rel=\"stylesheet\" href=\"/style.css\" type=\"text/css\"/>"

         :html-preamble "<nav>
                         <a href=\"/\">&lt; Acasă</a>
                         </nav>
                         <div id=\"updated\">Actualizat: %C</div>"

         :html-postamble "<hr/>
                <footer>
                  <div class=\"copyright-container\">
                    <div class=\"copyright\">
                      Copyright &copy; 2023 Eugen Stan - unele drepturi rezervate<br/>
                      Conținut disponibil sub licență
                      <a rel=\"license\" href=\"http://creativecommons.org/licenses/by-sa/4.0/\">
                        CC-BY-SA 4.0
              </a> dacă nu este specificat altfel.
            </div>
            <div class=\"cc-badge\">
              <a rel=\"license\" href=\"http://creativecommons.org/licenses/by-sa/4.0/\">
                <img alt=\"Licență Creative Commons\"
                     src=\"https://i.creativecommons.org/l/by-sa/4.0/88x31.png\" />
              </a>
            </div>
          </div>

          <div class=\"generated\">
            Creat cu %c pe un sistem <a href=\"https://www.gnu.org\">GNU</a>/<a href=\"https://www.kernel.org/\">Linux</a>
          </div>
        </footer>")))

;; generate the site output

(org-publish-all t)

(message "Build complete!")
