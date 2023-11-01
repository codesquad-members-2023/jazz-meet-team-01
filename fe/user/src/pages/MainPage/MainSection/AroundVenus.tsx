import { useEffect, useState } from 'react';
import { SwiperSlide } from 'swiper/react';
import { getAroundVenues } from '~/apis/venue';
import { BASIC_COORDINATE } from '~/constants/COORDINATE';
import { AroundVenue } from '~/types/api.types';
import { CardList } from './CardList';
import { CardListHeader } from './CardList/CardListHeader';
import { Cards } from './CardList/Cards';
import { AroundVenueCard } from './CardList/Cards/AroundVenueCard';

export const AroundVenus: React.FC = () => {
  const [aroundVenues, setAroundVenues] = useState<AroundVenue[]>();

  const updateAroundVenues = async (position: GeolocationPosition) => {
    const { latitude, longitude } = position.coords;
    const aroundVenues = await getAroundVenues({ latitude, longitude });
    setAroundVenues(aroundVenues);
  };

  const handlePermissionDenied = async () => {
    const aroundVenues = await getAroundVenues(BASIC_COORDINATE);
    setAroundVenues(aroundVenues);
  };

  useEffect(() => {
    navigator.geolocation.getCurrentPosition(
      updateAroundVenues,
      handlePermissionDenied,
    );
  }, []);

  return (
    <CardList>
      <CardListHeader title="주변 공연장" />

      <Cards>
        {aroundVenues &&
          aroundVenues.map((aroundVenue) => (
            <SwiperSlide key={aroundVenue.id}>
              <AroundVenueCard aroundVenue={aroundVenue} />
            </SwiperSlide>
          ))}
      </Cards>
    </CardList>
  );
};
