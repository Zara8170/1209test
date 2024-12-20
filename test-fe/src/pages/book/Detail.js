import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import apiClient from "../../api/api";

export default function Detail() {
  const { id } = useParams();
  const [book, setBook] = useState({});

  useEffect(() => {
    const fetchBook = async () => {
      try {
        const response = await apiClient.get(`/api/books/${id}`);
        setBook(response.data);
      } catch (error) {
        console.error("Failed to fetch book:", error);
      }
    };
    fetchBook();
  }, [id]);

  return (
    <div className="p-5 bg-gray-100 min-h-screen flex justify-center items-center">
      <div className="bg-white p-6 rounded-lg shadow-lg w-full md:w-2/3 lg:w-1/2">
        <div className="flex justify-center mb-6">
          <img
            src={book.imgsrc}
            alt={book.title}
            className="max-h-80 object-cover rounded-lg"
          />
        </div>
        <h2 className="text-3xl font-bold mb-4 text-center">{book.title}</h2>
        <p className="text-gray-600 mb-4">
          Category: {book.categoryName || "Unknown"}
        </p>
      </div>
    </div>
  );
}
