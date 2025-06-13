// src/components/LanguageSelector.tsx
import React, { useState } from "react";
import { useTranslation } from "react-i18next";
import i18n from "../../i18n";

type Lang = "pt" | "en" | "es";

const flags: Record<Lang, string> = {
  pt: "/flags/brazil.png",
  en: "/flags/usa.png",
  es: "/flags/spain.png",
};

export function LanguageSelector() {
  const { t } = useTranslation();
  const [lang, setLang] = useState<Lang>(i18n.language as Lang);
  const [open, setOpen] = useState(false);

  const handleChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    const newLang = e.target.value as Lang;
    setLang(newLang);
    i18n.changeLanguage(newLang);
  };

  return (
    <button
      className="relative inline-block cursor-pointer"
      onClick={() => setOpen((prev) => !prev)}
    >
      <img
        src={flags[lang]}
        alt={`${lang} flag`}
        className="w-6 h-6 rounded-full object-cover"
      />
      {open && (
        <div className="absolute left-0 mt-2 w-24 bg-white border border-gray-300 rounded shadow-lg z-10">
          {Object.entries(flags)
            .filter(([code]) => code !== lang)
            .map(([code, url]) => (
              <button
                key={code}
                className="flex items-center w-full px-2 py-1 hover:bg-gray-100"
                onClick={() => {
                  setLang(code as Lang);
                  i18n.changeLanguage(code);
                  setOpen(false);
                }}
              >
                <img
                  src={url}
                  alt={`${code} flag`}
                  className="w-6 h-6 rounded-full object-cover"
                />
              </button>
            ))}
        </div>
      )}
    </button>
  );
}
