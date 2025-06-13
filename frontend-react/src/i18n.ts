import i18n from "i18next";
import { initReactI18next } from "react-i18next";
import translations from "./locales/translation.json";
import type { Lang } from "./shared/types/Lang";

const supportedLangs: Lang[] = ["en", "es", "pt"];

const resources = Object.fromEntries(
  supportedLangs.map((lang) => [
    lang,
    {
      translation: Object.fromEntries(
        Object.entries(translations).map(([key, value]) => [
          key,
          (value as Record<Lang, string>)[lang],
        ])
      ),
    },
  ])
) as Record<Lang, { translation: Record<string, string> }>;

i18n.use(initReactI18next).init({
  resources,
  lng: "pt", // idioma padrão conforme você pediu
  fallbackLng: "en", // se faltar alguma chave em pt, usa en
  interpolation: {
    escapeValue: false, // não escape HTML
  },
});

export default i18n;
