document.addEventListener("DOMContentLoaded", function () {
    console.log("działa hehe")

    /**
     * Form Select
     */
    class FormSelect {
        constructor($el) {
            this.$el = $el;
            this.options = [...$el.children];
            this.init();
        }

        init() {
            this.createElements();
            this.addEvents();
            this.$el.parentElement.removeChild(this.$el);
        }

        createElements() {
            // Input for value
            this.valueInput = document.createElement("input");
            this.valueInput.type = "text";
            this.valueInput.name = this.$el.name;

            // Dropdown container
            this.dropdown = document.createElement("div");
            this.dropdown.classList.add("dropdown");

            // List container
            this.ul = document.createElement("ul");

            // All list options
            this.options.forEach((el, i) => {
                const li = document.createElement("li");
                li.dataset.value = el.value;
                li.innerText = el.innerText;

                if (i === 0) {
                    // First clickable option
                    this.current = document.createElement("div");
                    this.current.innerText = el.innerText;
                    this.dropdown.appendChild(this.current);
                    this.valueInput.value = el.value;
                    li.classList.add("selected");
                }

                this.ul.appendChild(li);
            });

            this.dropdown.appendChild(this.ul);
            this.dropdown.appendChild(this.valueInput);
            this.$el.parentElement.appendChild(this.dropdown);
        }

        addEvents() {
            this.dropdown.addEventListener("click", e => {
                const target = e.target;
                this.dropdown.classList.toggle("selecting");

                // Save new value only when clicked on li
                if (target.tagName === "LI") {
                    this.valueInput.value = target.dataset.value;
                    this.current.innerText = target.innerText;
                }
            });
        }
    }

    document.querySelectorAll(".form-group--dropdown select").forEach(el => {
        new FormSelect(el);
    });

    /**
     * Hide elements when clicked on document
     */
    document.addEventListener("click", function (e) {
        const target = e.target;
        const tagName = target.tagName;

        if (target.classList.contains("dropdown")) return false;

        if (tagName === "LI" && target.parentElement.parentElement.classList.contains("dropdown")) {
            return false;
        }

        if (tagName === "DIV" && target.parentElement.classList.contains("dropdown")) {
            return false;
        }

        document.querySelectorAll(".form-group--dropdown .dropdown").forEach(el => {
            el.classList.remove("selecting");
        });
    });

    /**
     * Switching between form steps
     */
    class FormSteps {
        constructor(form) {
            this.$form = form;
            this.$next = form.querySelectorAll(".next-step");
            this.$prev = form.querySelectorAll(".prev-step");
            this.$step = form.querySelector(".form--steps-counter span");
            this.currentStep = 1;

            this.$stepInstructions = form.querySelectorAll(".form--steps-instructions p");
            const $stepForms = form.querySelectorAll("form > div");
            this.slides = [...this.$stepInstructions, ...$stepForms];

            this.init();
        }

        /**
         * Init all methods
         */
        init() {
            this.events();
            this.updateForm();
        }

        /**
         * All events that are happening in form
         */
        events() {
            // Next step
            this.$next.forEach(btn => {
                btn.addEventListener("click", e => {
                    e.preventDefault();
                    this.currentStep++;
                    this.updateForm();
                    if (this.currentStep == 5) {
                        showSummary() // added by me
                    }
                });
            });

            // Previous step
            this.$prev.forEach(btn => {
                btn.addEventListener("click", e => {
                    e.preventDefault();
                    this.currentStep--;
                    this.updateForm();
                });
            });


            // Form submit
            this.$form.querySelector("form").addEventListener("btnSubmit", e => this.submit(e));
        }

        /**
         * Update form front-end
         * Show next or previous section etc.
         */
        updateForm() {
            this.$step.innerText = this.currentStep;

            // TODO: Validation

            this.slides.forEach(slide => {
                slide.classList.remove("active");

                if (slide.dataset.step == this.currentStep) {
                    slide.classList.add("active");
                }
            });

            this.$stepInstructions[0].parentElement.parentElement.hidden = this.currentStep >= 5;
            this.$step.parentElement.hidden = this.currentStep >= 5;

            // TODO: get data from inputs and show them in summary
        }
    }

    const form = document.querySelector(".form--steps");
    if (form !== null) {
        new FormSteps(form);
    }

    function showSummary() {

        $("#streetSummary").html(document.getElementById("street").value)
        $("#citySummary").html(document.getElementById("city").value)
        $("#postcodeSummary").html(document.getElementById("postcode").value)
        $("#phoneSummary").html(document.getElementById("phone").value)
        $("#dateSummary").html(document.getElementById("date").value)
        $("#timeSummary").html(document.getElementById("time").value)
        $("#infoSummary").html(document.getElementById("info").value)

        let summary = '';
        if (document.getElementById("inst1").checked) summary = "Dbam o Zdrowie"
        if (document.getElementById("inst2").checked) summary = "Bez domu"
        if (document.getElementById("inst3").checked) summary = "A kogo"
        if (document.getElementById("inst4").checked) summary = "Dla dzieci"
        if (document.getElementById("inst5").checked) summary = "Iskierka"
        if (document.getElementById("inst6").checked) summary = "Zdąrzyć z pomocą"
        $("#instSummary").html(summary)

        categorySummary();
    }

    function categorySummary() {

        let summary = '';
        summary += document.getElementById("quantity").value
        if (document.getElementById("quantity").value < 2) summary += " worek"
        else if (document.getElementById("quantity").value >= 2 && document.getElementById("quantity").value < 5) summary += " worki"
        else summary += " worków"

        if (document.getElementById("myForm1").checked) summary += " Zabawek, "
        if (document.getElementById("myForm2").checked) summary += " Ubrań nadających się do ponownego użycia, "
        if (document.getElementById("myForm3").checked) summary += " Ubrań do ponownego użycia, "
        if (document.getElementById("myForm4").checked) summary += " Ubrań do wyrzucenia, "
        if (document.getElementById("myForm3").checked) summary += " Innych rzeczy, "
        $("#categorySummary").html(summary)
    }
})

