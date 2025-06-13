type ButtonPrimaryProps = {
  title: string;
};

export function ButtonPrimary({ title }: ButtonPrimaryProps) {
  return (
    <button
      type="button"
      className="
              px-3 py-1.5
              bg-indigo-600 text-white font-semibold
              rounded-md
              hover:bg-indigo-700
              transition
              min-w-[120px] text-center cursor-pointer
            "
    >
      <span>{title}</span>
    </button>
  );
}
