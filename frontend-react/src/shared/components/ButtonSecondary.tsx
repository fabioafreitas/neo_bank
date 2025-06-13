type ButtonSecondaryProps = {
  title: string;
};

export function ButtonSecondary({ title }: ButtonSecondaryProps) {
  return (
    <button
      type="button"
      className="
              px-3 py-1.5
              bg-gray-300 text-gray-700 font-medium
              rounded-md
              hover:bg-gray-400
              transition
              min-w-[120px] text-center cursor-pointer
            "
    >
      {title}
    </button>
  );
}
